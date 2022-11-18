//
//  RTMNoticeModel.h
//  veRTC_Demo
//
//  Created by on 2021/12/22.
//  
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface RTMNoticeModel : NSObject

@property (nonatomic, copy) NSString *eventName;
@property (nonatomic, copy) NSString *timestamp;
@property (nonatomic, copy) NSDictionary *data;

@end

NS_ASSUME_NONNULL_END
