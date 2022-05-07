//
//  VoiceChatSeatItemView.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/11/29.
//  Copyright © 2021 . All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VoiceChatRTMManager.h"

@interface VoiceChatSeatItemView : UIView

@property (nonatomic, assign) NSInteger index;

@property (nonatomic, strong) VoiceChatSeatModel *seatModel;

@property (nonatomic, copy) void (^clickBlock)(VoiceChatSeatModel *seatModel);

@end
