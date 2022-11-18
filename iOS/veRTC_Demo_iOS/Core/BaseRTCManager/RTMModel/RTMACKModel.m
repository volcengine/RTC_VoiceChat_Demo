//
//  RTMACKModel.m
//  veRTC_Demo
//
//  Created by on 2021/12/21.
//  
//

#import "RTMACKModel.h"
#import "NetworkingTool.h"
#import <YYModel/YYModel.h>

@implementation RTMACKModel

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{@"requestID" : @"request_id",
             @"response" : @"response"};
}

+ (instancetype)modelWithMessageData:(id)data {
    RTMACKModel *ackModel = [self yy_modelWithJSON:data];
    if (ackModel && ackModel.code != RTMStatusCodeSuccess) {
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
    RTMACKModel *ackModel = [[self alloc] init];
    ackModel.code = RTMStatusCodeBadServerResponse;
    ackModel.message = [NetworkingTool messageFromResponseCode:ackModel.code];
    return ackModel;
}

- (BOOL)result {
    if (self.code == RTMStatusCodeSuccess) {
        return YES;
    } else {
        return NO;
    }
}

@end
