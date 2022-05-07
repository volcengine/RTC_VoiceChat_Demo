//
//  VoiceChatRoomModel.m
//  SceneRTCDemo
//
//  Created by bytedance on 2021/3/16.
//

#import "VoiceChatRoomModel.h"

@implementation VoiceChatRoomModel

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{@"appID" : @"app_id",
             @"roomID" : @"room_id",
             @"roomName" : @"room_name",
             @"hostUid" : @"host_user_id",
             @"hostName" : @"host_user_name",
             @"audienceCount" : @"audience_count",
             @"enableAudienceApply" : @"enable_audience_interact_apply",
    };
}

- (void)setExt:(NSString *)ext {
    if (_ext != ext) {
        _ext = ext;
        if (NOEmptyStr(ext)) {
            self.extDic = [VoiceChatRoomModel dictionaryWithJsonString:ext];
        } else {
            self.extDic = @{};
        }
    }
}

+ (NSDictionary *)dictionaryWithJsonString:(NSString *)jsonString {
   if (jsonString == nil) {
       return nil;
   }

   NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
   NSError *err;
   NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:jsonData
                                                       options:NSJSONReadingMutableContainers
                                                         error:&err];
   if(err) {
       NSLog(@"json解析失败：%@",err);
       return nil;
   }
   return dic;
}


@end
