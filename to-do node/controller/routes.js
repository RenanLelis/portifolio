const express = require('express');
const mid = require('./middleware/middleware');
const siteController = require('./siteController');
const authController = require('./authController');

const router = express.Router();

//User Login
router.post('/api/auth/login', authController.login)

//Forget Password
router.post('/api/auth/forgotPassword', authController.getNewPasswordCode)

//Register the new password vadating the code
router.post('/api/auth/newPassword', mid.validaRecaptcha, authController.registerNewPassword)

//Register new user
router.post('/api/auth/registerUser', authController.registerUser)

//Activate User
router.post('/api/auth/activateUser', authController.activateUser)

module.exports = router;

