//
//  VoiceChatRoomTableView.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/5/18.
//  Copyright © 2021 . All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VoiceChatRoomCell.h"
@class VoiceChatRoomTableView;

NS_ASSUME_NONNULL_BEGIN

@protocol VoiceChatRoomTableViewDelegate <NSObject>

- (void)VoiceChatRoomTableView:(VoiceChatRoomTableView *)VoiceChatRoomTableView didSelectRowAtIndexPath:(VoiceChatRoomModel *)model;

@end

@interface VoiceChatRoomTableView : UIView

@property (nonatomic, copy) NSArray *dataLists;

@property (nonatomic, weak) id<VoiceChatRoomTableViewDelegate> delegate;


@end

NS_ASSUME_NONNULL_END
