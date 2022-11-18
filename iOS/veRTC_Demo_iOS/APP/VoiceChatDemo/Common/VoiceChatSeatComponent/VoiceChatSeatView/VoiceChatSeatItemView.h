//
//  VoiceChatSeatItemView.h
//  veRTC_Demo
//
//  Created by on 2021/11/29.
//  
//

#import <UIKit/UIKit.h>
#import "VoiceChatRTMManager.h"

@interface VoiceChatSeatItemView : UIView

@property (nonatomic, assign) NSInteger index;

@property (nonatomic, strong) VoiceChatSeatModel *seatModel;

@property (nonatomic, copy) void (^clickBlock)(VoiceChatSeatModel *seatModel);

@end
