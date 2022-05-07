//
//  VoiceChatIMModel.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/5/28.
//  Copyright © 2021 . All rights reserved.
//

#import <Foundation/Foundation.h>
#import "VoiceChatRTMManager.h"

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatIMModel : NSObject

@property (nonatomic, assign) BOOL isJoin;

@property (nonatomic, strong) NSString *message;

@property (nonatomic, strong) VoiceChatUserModel *userModel;

@end

NS_ASSUME_NONNULL_END
