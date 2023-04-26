// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatRoomParamInfoModel : NSObject

// 上行网络质量评分
@property (nonatomic, assign) NSInteger txQuality;

// 下行网络质量评分
@property (nonatomic, assign) NSInteger rxQuality;

// 数据传输往返时延。
@property (nonatomic, strong) NSString *rtt;

@end

NS_ASSUME_NONNULL_END
