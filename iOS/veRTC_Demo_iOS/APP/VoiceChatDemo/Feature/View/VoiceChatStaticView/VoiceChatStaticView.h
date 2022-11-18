//
//  VoiceChatStaticView.h
//  veRTC_Demo
//
//  Created by on 2021/11/29.
//  
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
