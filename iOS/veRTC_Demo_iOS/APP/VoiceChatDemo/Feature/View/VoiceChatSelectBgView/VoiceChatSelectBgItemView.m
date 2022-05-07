//
//  VoiceChatSelectBgItemView.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/11/26.
//  Copyright © 2021 . All rights reserved.
//

#import "VoiceChatSelectBgItemView.h"

@interface VoiceChatSelectBgItemView ()

@property (nonatomic, strong) UIImageView *selectImageView;
@property (nonatomic, strong) UIImageView *bgImageView;
@property (nonatomic, assign) NSInteger index;

@end

@implementation VoiceChatSelectBgItemView

- (instancetype)initWithIndex:(NSInteger)index {
    self = [super init];
    if (self) {
        _index = index;
        self.layer.masksToBounds = YES;
        self.layer.cornerRadius = 2;
        
        [self addSubview:self.bgImageView];
        [self.bgImageView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.equalTo(self);
        }];
        
        [self addSubview:self.selectImageView];
        [self.selectImageView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.equalTo(self);
        }];
        
        self.bgImageView.image = [UIImage imageNamed:[self getBackgroundSmallImageName] bundleName:HomeBundleName];
        self.isSelected = NO;
    }
    return self;
}

- (void)setIsSelected:(BOOL)isSelected {
    _isSelected = isSelected;
    
    if (isSelected) {
        self.selectImageView.hidden = NO;
        
    } else {
        self.selectImageView.hidden = YES;
    }
}

#pragma mark - Publish Action

- (NSString *)getBackgroundImageName {
    return [self getBackgroundImageNames][_index];
}

- (NSString *)getBackgroundSmallImageName {
    return [self getSmallBackgroundImageNames][_index];
}

#pragma mark - Private Action

- (NSArray *)getBackgroundImageNames {
    return @[@"voicechat_background_0.jpg",
             @"voicechat_background_1.jpg",
             @"voicechat_background_2.jpg"];
}

- (NSArray *)getSmallBackgroundImageNames {
    return @[@"voicechat_background_small_0",
             @"voicechat_background_small_1",
             @"voicechat_background_small_2"];
}

#pragma mark - Getter

- (UIImageView *)bgImageView {
    if (!_bgImageView) {
        _bgImageView = [[UIImageView alloc] init];
        _bgImageView.contentMode = UIViewContentModeScaleAspectFill;
        _bgImageView.clipsToBounds = YES;
    }
    return _bgImageView;
}

- (UIImageView *)selectImageView {
    if (!_selectImageView) {
        _selectImageView = [[UIImageView alloc] init];
        _selectImageView.image = [UIImage imageNamed:@"voicechat_bg_icon" bundleName:HomeBundleName];
        _selectImageView.hidden = YES;
    }
    return _selectImageView;
}

@end
