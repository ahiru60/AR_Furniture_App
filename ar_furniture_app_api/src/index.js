const express = require('express');
const bodyParser = require('body-parser');
const userController = require('./userController');
const furnitureController = require('./furnitureController');

const app = express();
const port = 3000;

app.use(bodyParser.json());

app.use('/users', userController);
app.use('/furniture', furnitureController);

app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});
