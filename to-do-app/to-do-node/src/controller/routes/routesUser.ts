import express from 'express';
import userController from '../userController';
import mid from '../middleware/middleware'

const router = express.Router();

router.put('/api/user/profile', mid.validateAuth, userController.updateUserProfile)
router.put('/api/user/password', mid.validateAuth, userController.updateUserPassword)

export default { router };