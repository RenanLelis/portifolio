import express from 'express';
import authController from '../authController';
// import mid from '../middleware/middleware'

const router = express.Router();

// router.post('/api/auth/login', mid.validateAuth, authController.login)
router.post('/api/auth/login', authController.login)
router.post('/api/auth/recoverpassword', authController.recoverPassword)
router.post('/api/auth/passwordreset', authController.resetPassword)
router.post('/api/auth/userregistration', authController.registerNewUser)
router.post('/api/auth/useractivation', authController.activateUser)
router.post('/api/auth/useractivationrequest', authController.requestUserActivation)

export default { router };