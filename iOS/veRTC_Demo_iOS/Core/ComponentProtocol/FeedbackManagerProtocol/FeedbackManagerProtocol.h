//
//  FeedbackManagerProtocol.h
//  veLogin
//
//  Created by   on 2022/8/23.
//

#import <Foundation/Foundation.h>
@class FeedbackManagerProtocol;

NS_ASSUME_NONNULL_BEGIN

@protocol FeedbackManagerDelegate <NSObject>

- (instancetype)protocol:(FeedbackManagerProtocol *)protocol initWithSuperView:(UIView *)superView;
        

@end

@interface FeedbackManagerProtocol : NSObject

/**
 * Initialization
 * @param superView Super view
 */
- (instancetype)initWithSuperView:(UIView *)superView;

@end

NS_ASSUME_NONNULL_END
