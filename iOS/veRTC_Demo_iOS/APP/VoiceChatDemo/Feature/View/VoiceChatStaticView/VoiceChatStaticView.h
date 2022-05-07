//
//  VoiceChatStaticView.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/11/29.
//  Copyright © 2021 . All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VoiceChatRTMManager.h"

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatStaticView : UIView

@property (nonatomic, strong) VoiceChatRoomModel *roomModel;

- (void)updatePeopleNum:(NSInteger)count;

- (void)updateParamInfoModel:(VoiceChatRoomParamInfoModel *)paramInfoModel;

@end

NS_ASSUME_NONNULL_END
