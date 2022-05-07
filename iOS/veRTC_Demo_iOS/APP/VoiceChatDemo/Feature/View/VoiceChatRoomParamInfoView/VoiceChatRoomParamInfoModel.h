//
//  VoiceChatRoomParamInfoModel.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/6/2.
//  Copyright © 2021 . All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatRoomParamInfoModel : NSObject

@property (nonatomic, strong) NSString *sendLossRate;
@property (nonatomic, strong) NSString *receivedLossRate;
@property (nonatomic, strong) NSString *rtt;

@end

NS_ASSUME_NONNULL_END
