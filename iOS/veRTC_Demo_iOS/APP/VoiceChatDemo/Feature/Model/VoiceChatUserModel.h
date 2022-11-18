//
//  VoiceChatUserModel.h
//  SceneRTCDemo
//
//  Created by on 2021/3/16.
//

#import "BaseUserModel.h"

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSInteger, UserStatus) {
    UserStatusDefault = 1,
    UserStatusActive,
    UserStatusApply,
    UserStatusInvite,
};

typedef NS_ENUM(NSInteger, UserRole) {
    UserRoleNone = 0,
    UserRoleHost = 1,
    UserRoleAudience,
};

typedef NS_ENUM(NSInteger, UserMic) {
    UserMicOff = 0,
    UserMicOn = 1,
};

@interface VoiceChatUserModel : BaseUserModel

@property (nonatomic, copy) NSString *roomID;

@property (nonatomic, assign) UserRole userRole;

@property (nonatomic, assign) UserStatus status;

@property (nonatomic, assign) UserMic mic;

@property (nonatomic, assign) NSInteger volume;

@property (nonatomic, assign) BOOL isSpeak;

@end

NS_ASSUME_NONNULL_END
