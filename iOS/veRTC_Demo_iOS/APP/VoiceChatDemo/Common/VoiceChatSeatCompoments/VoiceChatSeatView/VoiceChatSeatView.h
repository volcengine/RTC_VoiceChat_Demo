//
//  VoiceChatSeatView.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/11/29.
//  Copyright © 2021 . All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VoiceChatRTMManager.h"

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatSeatView : UIView

@property (nonatomic, copy) void (^clickBlock)(VoiceChatSeatModel *seatModel);

@property (nonatomic, copy) NSArray<VoiceChatSeatModel *> *seatList;

- (void)addSeatModel:(VoiceChatSeatModel *)seatModel;

- (void)removeUserModel:(VoiceChatUserModel *)userModel;

- (void)updateSeatModel:(VoiceChatSeatModel *)seatModel;

- (void)updateSeatVolume:(NSDictionary *)volumeDic;

@end

NS_ASSUME_NONNULL_END
