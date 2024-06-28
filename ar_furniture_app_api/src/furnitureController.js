const express = require('express');
const router = express.Router();
const db = require('./db');

// Get all furniture
router.get('/', (req, res) => {
    db.query('SELECT * FROM furniture', (err, results) => {
        if (err) {
            return res.status(500).json({ error: err });
        }
        res.json(results);
    });
});

// Get furniture by ID
router.get('/:id', (req, res) => {
    const furnitureId = req.params.id;
    db.query('SELECT * FROM furniture WHERE FurnitureID = ?', [furnitureId], (err, results) => {
        if (err) {
            return res.status(500).json({ error: err });
        }
        res.json(results[0]);
    });
});

// Create a new furniture
router.post('/', (req, res) => {
    const furniture = req.body;
    db.query('INSERT INTO furniture SET ?', furniture, (err, results) => {
        if (err) {
            return res.status(500).json({ error: err });
        }
        res.status(201).json({ id: results.insertId });
    });
});

// Update furniture
router.put('/:id', (req, res) => {
    const furnitureId = req.params.id;
    const furniture = req.body;
    db.query('UPDATE furniture SET ? WHERE FurnitureID = ?', [furniture, furnitureId], (err, results) => {
        if (err) {
            return res.status(500).json({ error: err });
        }
        res.json({ affectedRows: results.affectedRows });
    });
});

// Delete furniture
router.delete('/:id', (req, res) => {
    const furnitureId = req.params.id;
    db.query('DELETE FROM furniture WHERE FurnitureID = ?', [furnitureId], (err, results) => {
        if (err) {
            return res.status(500).json({ error: err });
        }
        res.json({ affectedRows: results.affectedRows });
    });
});

module.exports = router;
