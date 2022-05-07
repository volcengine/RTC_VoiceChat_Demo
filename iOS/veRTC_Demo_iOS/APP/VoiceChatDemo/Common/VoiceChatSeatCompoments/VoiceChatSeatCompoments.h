//
//  VoiceChatSeatCompoments.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/12/1.
//  Copyright © 2021 . All rights reserved.
//

#import <Foundation/Foundation.h>
#import "VoiceChatSheetView.h"
@class VoiceChatSeatCompoments;

NS_ASSUME_NONNULL_BEGIN

@protocol VoiceChatSeatDelegate <NSObject>

- (void)voiceChatSeatCompoments:(VoiceChatSeatCompoments *)voiceChatSeatCompoments
                    clickButton:(VoiceChatSeatModel *)seatModel
                    sheetStatus:(VoiceChatSheetStatus)sheetStatus;

@end

@interface VoiceChatSeatCompoments : NSObject

@property (nonatomic, weak) id<VoiceChatSeatDelegate> delegate;

- (instancetype)initWithSuperView:(UIView *)superView;

- (void)showSeatView:(NSArray<VoiceChatSeatModel *> *)seatList
      loginUserModel:(VoiceChatUserModel *)loginUserModel;

- (void)addSeatModel:(VoiceChatSeatModel *)seatModel;

- (void)removeUserModel:(VoiceChatUserModel *)userModel;

- (void)updateSeatModel:(VoiceChatSeatModel *)seatModel;

- (void)updateSeatVolume:(NSDictionary *)volumeDic;

@end

NS_ASSUME_NONNULL_END
