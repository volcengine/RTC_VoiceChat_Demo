// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "RTSACKModel.h"
#import "NetworkingTool.h"
#import <YYModel/YYModel.h>

@implementation RTSACKModel

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{@"requestID" : @"request_id",
             @"response" : @"response"};
}

+ (instancetype)modelWithMessageData:(id)data {
    RTSACKModel *ackModel = [self yy_modelWithJSON:data];
    if (ackModel && ackModel.code != RTSStatusCodeSuccess) {
        NSString *message = [NetworkingTool messageFromResponseCode:ackModel.code];
        if (message && message.length > 0) {
            ackModel.message = message;
        }
    }
    if (!ackModel) {
        ackModel = [self badServerResponse];
    }
    return ackModel;
}

+ (instancetype)badServerResponse {
    RTSACKModel *ackModel = [[self alloc] init];
    ackModel.code = RTSStatusCodeBadServerResponse;
    ackModel.message = [NetworkingTool messageFromResponseCode:ackModel.code];
    return ackModel;
}

- (BOOL)result {
    if (self.code == RTSStatusCodeSuccess) {
        return YES;
    } else {
        return NO;
    }
}

@end
