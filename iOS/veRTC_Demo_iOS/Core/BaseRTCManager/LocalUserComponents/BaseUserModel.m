//
//  BaseUserModel.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/6/28.
//  Copyright © 2021 . All rights reserved.
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
}

- (instancetype)initWithCoder:(NSCoder *)coder {
    self = [super init];
    if (self) {
        self.uid = [coder decodeObjectOfClass:[NSString class] forKey:@"uid"];
        self.name = [coder decodeObjectOfClass:[NSString class] forKey:@"name"];
    }
    return self;
}

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{@"uid" : @"user_id",
             @"name" : @"user_name"};
}

- (NSString *)voiceUid {
    return self.uid;
}

@end
