//
//  BaseUserModel.m
//  veRTC_Demo
//
//  Created by on 2021/6/28.
//  
//

#import "BaseUserModel.h"
#import "NetworkingTool.h"

@implementation BaseUserModel

+ (BOOL)supportsSecureCoding {
    return YES;
}

- (void)encodeWithCoder:(NSCoder *)coder {
    [coder encodeObject:self.uid forKey:@"uid"];
    [coder encodeObject:self.name forKey:@"name"];
    [coder encodeObject:self.loginToken forKey:@"loginToken"];
}

- (instancetype)initWithCoder:(NSCoder *)coder {
    self = [super init];
    if (self) {
        self.uid = [coder decodeObjectOfClass:[NSString class] forKey:@"uid"];
        self.name = [coder decodeObjectOfClass:[NSString class] forKey:@"name"];
        self.loginToken = [coder decodeObjectOfClass:[NSString class] forKey:@"loginToken"];
    }
    return self;
}

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{@"uid" : @"user_id",
             @"name" : @"user_name",
             @"loginToken" : @"login_token"
    };
}

@end
