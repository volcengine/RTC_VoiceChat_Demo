//
//  Constants.h
//  quickstart
//

#ifndef Constants_h
#define Constants_h

/*
 业务服务器域名，需要客户自己搭建
 例如：http://xx/xx
 在 NetworkingManager 类中封装了 passwordFreeLogin、changeUserName、joinRTM 接口
 joinRTM 接口返回的 app_id、rtm_token、server_url、
 server_signature 等数据，用于加入RTM房间使用。
 */
#define LoginUrl <#url#>

static NSString * _Nullable const NotificationLoginExpired = @"NotificationLoginExpired";

#define SCREEN_WIDTH  ([UIScreen mainScreen].bounds.size.width)

#define SCREEN_HEIGHT ([UIScreen mainScreen].bounds.size.height)

#define WeakSelf __weak typeof(self) wself = self;

#define StrongSelf __strong __typeof(self) sself = wself;

#define  IsEmptyStr(string) (string == nil || string == NULL ||[string isKindOfClass:[NSNull class]]|| [string isEqualToString:@""] ||[[string stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]] length]==0 ? YES : NO)

#define  NOEmptyStr(string) (string == nil || string == NULL ||[string isKindOfClass:[NSNull class]] || [string isEqualToString: @""]  ||[[string stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]] length]==0 ? NO : YES)

#ifndef dispatch_queue_async_safe
#define dispatch_queue_async_safe(queue, block)\
    if (dispatch_queue_get_label(DISPATCH_CURRENT_QUEUE_LABEL) == dispatch_queue_get_label(queue)) {\
        block();\
    } else {\
        dispatch_async(queue, block);\
    }
#endif

#endif /* Constants_h */

