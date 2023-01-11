//
//  FeedbackManagerProtocol.m
//  veLogin
//
//  Created by   on 2022/8/23.
//

#import "FeedbackManagerProtocol.h"

@interface FeedbackManagerProtocol ()

@property (nonatomic, strong) id<FeedbackManagerDelegate> feedbackDelegate;

@end

@implementation FeedbackManagerProtocol

- (instancetype)initWithSuperView:(UIView *)superView {
    NSObject<FeedbackManagerDelegate> *feedback = [[NSClassFromString(@"FeedbackHome") alloc] init];
    if (feedback) {
        self.feedbackDelegate = feedback;
    }
    
    if ([self.feedbackDelegate respondsToSelector:@selector(protocol:initWithSuperView:)]) {
        return [self.feedbackDelegate protocol:self
                             initWithSuperView:superView];
    } else {
        return nil;
    }
}

@end
