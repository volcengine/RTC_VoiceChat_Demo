//
//  VoiceChatSeatView.h
//  veRTC_Demo
//
//  Created by on 2021/11/29.
//  
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

- (void)updateLocalSeatVolume:(NSInteger)volume;

- (void)updateSeatVolume:(NSDictionary *)volumeDic;

@end

NS_ASSUME_NONNULL_END
