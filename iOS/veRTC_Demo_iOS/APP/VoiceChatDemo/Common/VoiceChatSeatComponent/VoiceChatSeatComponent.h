//
//  VoiceChatSeatComponent.h
//  veRTC_Demo
//
//  Created by on 2021/12/1.
//  
//

#import <Foundation/Foundation.h>
#import "VoiceChatSheetView.h"
@class VoiceChatSeatComponent;

NS_ASSUME_NONNULL_BEGIN

@protocol VoiceChatSeatDelegate <NSObject>

- (void)voiceChatSeatComponent:(VoiceChatSeatComponent *)voiceChatSeatComponent
                    clickButton:(VoiceChatSeatModel *)seatModel
                    sheetStatus:(VoiceChatSheetStatus)sheetStatus;

@end

@interface VoiceChatSeatComponent : NSObject

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
