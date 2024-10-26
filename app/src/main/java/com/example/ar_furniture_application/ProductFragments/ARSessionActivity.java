package com.example.ar_furniture_application.ProductFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import com.example.ar_furniture_application.R;
import com.google.ar.core.Anchor;
import com.google.ar.core.Config;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Sceneform;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.CameraStream;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.lang.ref.WeakReference;

public class ARSessionActivity extends AppCompatActivity implements
        FragmentOnAttachListener,
        BaseArFragment.OnTapArPlaneListener,
        BaseArFragment.OnSessionConfigurationListener,
        ArFragment.OnViewCreatedListener {

    private ArFragment arFragment;
    private Renderable modelRenderable;
    private ViewRenderable viewRenderable;
    private LinearLayout loadingProgress;
    private SeekBar horizontalAngleSeekBar, verticalAngleSeekBar, heightSeekBar;
    private ImageButton resetButton, deleteButton;
    private String furnitureName;
    private String modelURL = "https://firebasestorage.googleapis.com/v0/b/ar-furniture-app-341b3.appspot.com/o/office_desk_-_18mb.glb?alt=media&token=65e33092-3a39-46a9-bcac-0acc381cde0f";
    private double scaleToWorld = 1.0;

    private float previousRotationAngleX = 0f;
    private float previousRotationAngleY = 0f;

    private static final float MIN_MODEL_HEIGHT = 0.0f;
    private static final float MAX_MODEL_HEIGHT = 1.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arsession);

        initializeUIElements();
        retrieveIntentData();
        setupListeners();

        getSupportFragmentManager().addFragmentOnAttachListener(this);

        if (savedInstanceState == null) {
            if (Sceneform.isSupported(this)) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.arFragment, ArFragment.class, null)
                        .commit();
            }
        }

        loadModels();
    }

    private void initializeUIElements() {
        horizontalAngleSeekBar = findViewById(R.id.seekBarRotation);
        verticalAngleSeekBar = findViewById(R.id.seekBarRotationZ);
        heightSeekBar = findViewById(R.id.seekBarHeight);
        resetButton = findViewById(R.id.resetSeekersImageBtn);
        deleteButton = findViewById(R.id.deleteButton); // Add reference to delete button
        deleteButton.setEnabled(false);
        deleteButton.setColorFilter(ContextCompat.getColor(this,R.color.cool_purplee));
        loadingProgress = findViewById(R.id.loadingProgressBar);
        loadingProgress.setVisibility(View.VISIBLE); // Show loading indicator

        horizontalAngleSeekBar.setMax(360);
        horizontalAngleSeekBar.setProgress(180);
        verticalAngleSeekBar.setMax(360);
        verticalAngleSeekBar.setProgress(180);
        heightSeekBar.setMax(200);
        heightSeekBar.setProgress(100); // Middle position

        previousRotationAngleX = verticalAngleSeekBar.getProgress() - 180;
        previousRotationAngleY = horizontalAngleSeekBar.getProgress() - 180;
    }

    private void retrieveIntentData() {
        Intent intent = getIntent();
        if (intent.getStringExtra("modelURL") != null) {
            furnitureName = intent.getStringExtra("furnitureName");
            modelURL = intent.getStringExtra("modelURL");
            scaleToWorld = intent.getDoubleExtra("scaleToWorld", 1.0);
        }
    }

    private void setupListeners() {
        // Reset button listener
        resetButton.setOnClickListener(v -> {
            resetSeekBars();
            resetRotationAndHeight();
        });

        // Delete button listener
        deleteButton.setOnClickListener(v -> {
            deleteSelectedObject();
        });

        // Horizontal Angle SeekBar Listener
        horizontalAngleSeekBar.setOnSeekBarChangeListener(new SimpleSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateRotation(seekBar, 'Y');
            }
        });

        // Vertical Angle SeekBar Listener
        verticalAngleSeekBar.setOnSeekBarChangeListener(new SimpleSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateRotation(seekBar, 'X');
            }
        });

        // Height SeekBar Listener
        heightSeekBar.setOnSeekBarChangeListener(new SimpleSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateModelHeight();
            }
        });
    }

    private void resetSeekBars() {
        horizontalAngleSeekBar.setProgress(180);
        verticalAngleSeekBar.setProgress(180);
        heightSeekBar.setProgress(100); // Reset height SeekBar to default position

        previousRotationAngleX = 0f;
        previousRotationAngleY = 0f;
    }

    private void resetRotationAndHeight() {
        TransformableNode selectedNode = (TransformableNode) arFragment.getTransformationSystem().getSelectedNode();

        if (selectedNode != null) {
            selectedNode.setLocalRotation(Quaternion.identity());
            updateModelHeight();
        }
    }

    private void updateRotation(SeekBar seekBar, char axisName) {
        TransformableNode selectedNode = (TransformableNode) arFragment.getTransformationSystem().getSelectedNode();
        if (selectedNode == null) {
            return;
        }

        float newRotationAngle;
        float deltaAngle;
        Vector3 axis;
        if (axisName == 'X') {
            newRotationAngle = verticalAngleSeekBar.getProgress() - 180;
            deltaAngle = newRotationAngle - previousRotationAngleX;
            axis = Vector3.right();

            previousRotationAngleX = newRotationAngle;
        } else {
            newRotationAngle = horizontalAngleSeekBar.getProgress() - 180;
            deltaAngle = newRotationAngle - previousRotationAngleY;
            axis = Vector3.back();

            previousRotationAngleY = newRotationAngle;
        }

        Quaternion deltaRotation = Quaternion.axisAngle(axis, deltaAngle);
        Quaternion currentRotation = selectedNode.getLocalRotation();
        Quaternion newRotation = Quaternion.multiply(deltaRotation, currentRotation);
        selectedNode.setLocalRotation(newRotation);
    }

    @Override
    public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        if (fragment.getId() == R.id.arFragment) {
            arFragment = (ArFragment) fragment;
            arFragment.setOnTapArPlaneListener(this);
            arFragment.setOnViewCreatedListener(this);
            arFragment.setOnSessionConfigurationListener(this);

        }
    }

    @Override
    public void onViewCreated(ArSceneView arSceneView) {
        arFragment.setOnViewCreatedListener(null);
        arSceneView.getCameraStream().setDepthOcclusionMode(CameraStream.DepthOcclusionMode.DEPTH_OCCLUSION_ENABLED);
        // Add this line to set up the update listener
        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onSceneUpdate);
    }

    public void loadModels() {
        WeakReference<ARSessionActivity> weakActivity = new WeakReference<>(this);

        // Load the 3D model
        ModelRenderable.builder()
                .setSource(this, Uri.parse(modelURL))
                .setIsFilamentGltf(true)
                .setAsyncLoadEnabled(true)
                .build()
                .thenAccept(model -> {
                    ARSessionActivity activity = weakActivity.get();
                    if (activity != null) {
                        activity.modelRenderable = model;
                        activity.checkModelLoadingComplete();
                    }
                })
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load model", Toast.LENGTH_LONG).show();
                    loadingProgress.setVisibility(View.GONE);  // Hide progress bar on failure
                    return null;
                });

        // Load the view renderable
        ViewRenderable.builder()
                .setView(this, R.layout.view_model_title)
                .build()
                .thenAccept(viewRenderable -> {
                    ARSessionActivity activity = weakActivity.get();
                    if (activity != null) {
                        activity.viewRenderable = viewRenderable;
                        TextView textView = viewRenderable.getView().findViewById(R.id.text);
                        if (textView != null) {
                            textView.setText(furnitureName != null ? furnitureName : "Furniture");
                        }
                        activity.checkModelLoadingComplete();
                    }
                })
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load view renderable", Toast.LENGTH_LONG).show();
                    loadingProgress.setVisibility(View.GONE);  // Hide progress bar on failure
                    return null;
                });
    }

    private void checkModelLoadingComplete() {
        if (modelRenderable != null && viewRenderable != null) {
            loadingProgress.setVisibility(View.GONE);  // Hide loading indicator
        }
    }

    @Override
    public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
        if (modelRenderable == null || viewRenderable == null) {
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create the Anchor.
        Anchor anchor = hitResult.createAnchor();
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());

        // Create a TransformableNode to handle user interaction
        TransformableNode parentNode = new TransformableNode(arFragment.getTransformationSystem());
        parentNode.setParent(anchorNode);
        parentNode.getScaleController().setMaxScale(2.0f);
        parentNode.getScaleController().setMinScale(0.5f);

        // Create the model node and set its renderable
        TransformableNode modelNode = new TransformableNode(arFragment.getTransformationSystem());
        modelNode.setParent(parentNode);
        modelNode.setRenderable(modelRenderable);
        modelNode.setLocalScale(new Vector3(1f / (float) scaleToWorld, 1f / (float) scaleToWorld, 1f / (float) scaleToWorld));

        // Disable transformation on the modelNode since parentNode handles it
        modelNode.getTranslationController().setEnabled(false);
        modelNode.getRotationController().setEnabled(false);
        modelNode.getScaleController().setEnabled(false);

        // Node for the title (optional)
        Node titleNode = new Node();
        titleNode.setParent(modelNode);
        titleNode.setLocalPosition(new Vector3(0.0f, 1.0f, 0.0f));
        titleNode.setRenderable(viewRenderable);
        titleNode.setEnabled(true);

        // Set initial height
        updateModelHeight();

        // Set parentNode as the selected node
        arFragment.getTransformationSystem().selectNode(parentNode);
    }

    @Override
    public void onSessionConfiguration(Session session, Config config) {
        if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
            config.setDepthMode(Config.DepthMode.AUTOMATIC);
        }
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
    }

    // Method to update the model height
    private void updateModelHeight() {
        TransformableNode selectedNode = (TransformableNode) arFragment.getTransformationSystem().getSelectedNode();
        if (selectedNode == null) {
            return;
        }

        // Assuming the selected node is the parent node
        if (selectedNode.getChildren().size() > 0) {
            Node modelNode = selectedNode.getChildren().get(0); // Get the modelNode

            float progressRatio = heightSeekBar.getProgress() / (float) heightSeekBar.getMax();
            float newYPosition = MIN_MODEL_HEIGHT + (MAX_MODEL_HEIGHT - MIN_MODEL_HEIGHT) * progressRatio;

            Vector3 currentPosition = modelNode.getLocalPosition();
            Vector3 newPosition = new Vector3(currentPosition.x, newYPosition, currentPosition.z);
            modelNode.setLocalPosition(newPosition);
        }
    }

    private void deleteSelectedObject() {
        TransformableNode selectedNode = (TransformableNode) arFragment.getTransformationSystem().getSelectedNode();
        if (selectedNode != null) {
            // Deselect the node
            arFragment.getTransformationSystem().selectNode(null);
            Node currentNode = selectedNode;

            // Traverse up the parent hierarchy to find the AnchorNode
            while (currentNode != null && !(currentNode instanceof AnchorNode)) {
                currentNode = (Node) currentNode.getParent();
            }

            if (currentNode instanceof AnchorNode) {
                AnchorNode anchorNode = (AnchorNode) currentNode;

                // Remove the AnchorNode and its children from the scene
                anchorNode.getAnchor().detach(); // Detach the anchor from ARCore
                anchorNode.setParent(null); // Remove the node from the scene graph
            } else {
                // Handle the case where no AnchorNode is found
                Toast.makeText(this, "Unable to delete object", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private Node lastSelectedNode = null;

    private void onSceneUpdate(FrameTime frameTime) {
        Node currentSelectedNode = arFragment.getTransformationSystem().getSelectedNode();
        if (currentSelectedNode != lastSelectedNode) {
            lastSelectedNode = currentSelectedNode;
            if (currentSelectedNode != null) {
                deleteButton.setEnabled(true);
                deleteButton.setColorFilter(ContextCompat.getColor(this,R.color.plain_yellow));
            } else {
                deleteButton.setEnabled(false);
                deleteButton.setColorFilter(ContextCompat.getColor(this,R.color.cool_purplee));
            }
        }
    }

    private abstract static class SimpleSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // Optional override
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // Optional override
        }
    }
}
