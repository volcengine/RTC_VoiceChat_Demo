//
//  UserHeadView.m
//  veRTC_Demo
//
//  Created by on 2021/5/23.
//  
//

#import "UserHeadView.h"
#import "AvatarView.h"
#import "Masonry.h"

@interface UserHeadView ()

@property (nonatomic, strong) AvatarView *avatarView;

@end

@implementation UserHeadView

- (instancetype)init {
    self = [super init];
    if (self) {
        [self addSubview:self.avatarView];
        [self.avatarView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.size.mas_equalTo(CGSizeMake(80, 80));
            make.center.equalTo(self);
        }];
    }
    return self;
}

- (void)setNameString:(NSString *)nameString {
    _nameString = nameString;
    
    self.avatarView.text = nameString;
}

- (AvatarView *)avatarView {
    if (!_avatarView) {
        _avatarView = [[AvatarView alloc] init];
        _avatarView.layer.cornerRadius = 40;
        _avatarView.layer.masksToBounds = YES;
        _avatarView.fontSize = 32;
    }
    return _avatarView;
}


@end
