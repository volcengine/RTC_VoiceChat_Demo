//
//  VoiceChatRoomTopSelectView.h
//  veRTC_Demo
//
//  Created by on 2021/5/24.
//  
//

#import <UIKit/UIKit.h>
@class VoiceChatRoomTopSelectView;

NS_ASSUME_NONNULL_BEGIN

@protocol VoiceChatRoomTopSelectViewDelegate <NSObject>

- (void)VoiceChatRoomTopSelectView:(VoiceChatRoomTopSelectView *)VoiceChatRoomTopSelectView clickCancelAction:(id)model;

- (void)VoiceChatRoomTopSelectView:(VoiceChatRoomTopSelectView *)VoiceChatRoomTopSelectView clickSwitchItem:(BOOL)isAudience;

@end

@interface VoiceChatRoomTopSelectView : UIView

@property (nonatomic, weak) id<VoiceChatRoomTopSelectViewDelegate> delegate;

@property (nonatomic, copy) NSString *titleStr;

- (void)updateWithRed:(BOOL)isRed;

- (void)updateSelectItem:(BOOL)isLeft;

@end

NS_ASSUME_NONNULL_END
