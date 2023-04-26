// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "RTCJoinModel.h"
#import "NetworkingTool.h"

@implementation RTCJoinModel

+ (RTCJoinModel *)modelArrayWithClass:(NSString *)extraInfo
                                state:(NSInteger)state
                               roomId:(NSString *)roomId {
    NSDictionary *dic = [NetworkingTool decodeJsonMessage:extraInfo];
    NSInteger errorCode = state;
    NSInteger joinType = -1;
    if ([dic isKindOfClass:[NSDictionary class]]) {
        NSString *joinTypeStr = [NSString stringWithFormat:@"%@", dic[@"join_type"]];
        joinType = joinTypeStr.integerValue;
    }
    
    RTCJoinModel *joinModel = [[RTCJoinModel alloc] init];
    joinModel.roomId = roomId;
    joinModel.errorCode = errorCode;
    joinModel.joinType = joinType;
    return joinModel;
}


@end
