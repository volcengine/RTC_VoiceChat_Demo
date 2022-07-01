//
//  Constants.h
//  quickstart
//

#ifndef Constants_h
#define Constants_h

static NSString * _Nullable const NotificationLoginExpired = @"NotificationLoginExpired";

#define SCREEN_WIDTH  ([UIScreen mainScreen].bounds.size.width)

#define SCREEN_HEIGHT ([UIScreen mainScreen].bounds.size.height)

#define WeakSelf __weak typeof(self) wself = self;

#define StrongSelf __strong __typeof(self) sself = wself;

#define IsEmptyStr(string) (string == nil || string == NULL ||[string isKindOfClass:[NSNull class]]|| [string isEqualToString:@""] ||[[string stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]] length]==0 ? YES : NO)

#define NOEmptyStr(string) (string == nil || string == NULL ||[string isKindOfClass:[NSNull class]] || [string isEqualToString: @""]  ||[[string stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]] length]==0 ? NO : YES)

#ifndef dispatch_queue_async_safe
#define dispatch_queue_async_safe(queue, block)\
    if (dispatch_queue_get_label(DISPATCH_CURRENT_QUEUE_LABEL) == dispatch_queue_get_label(queue)) {\
        block();\
    } else {\
        dispatch_async(queue, block);\
    }
#endif

#endif /* Constants_h */

