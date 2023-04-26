// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "BaseIMComponent.h"
#import "BaseIMView.h"
#import "Masonry.h"
#import "DeviceInforTool.h"

@interface BaseIMComponent ()

@property (nonatomic, strong) BaseIMView *baseIMView;

@end

@implementation BaseIMComponent

- (instancetype)initWithSuperView:(UIView *)superView {
    self = [super init];
    if (self) {
        [superView addSubview:self.baseIMView];
        [self.baseIMView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(20);
            make.bottom.mas_equalTo(-10 - ([DeviceInforTool getVirtualHomeHeight] + 64));
            make.height.mas_equalTo(115);
            make.width.mas_equalTo(275);
        }];
    }
    return self;
}

#pragma mark - Publish Action

- (void)addIM:(BaseIMModel *)model {
    NSMutableArray *datas = [[NSMutableArray alloc] initWithArray:self.baseIMView.dataLists];
    [datas addObject:model];
    self.baseIMView.dataLists = [datas copy];
}

- (void)updaetHidden:(BOOL)isHidden {
    self.baseIMView.hidden = isHidden;
}

- (void)updateUserInteractionEnabled:(BOOL)isEnabled {
    self.baseIMView.userInteractionEnabled = isEnabled;
}

#pragma mark - Getter

- (BaseIMView *)baseIMView {
    if (!_baseIMView) {
        _baseIMView = [[BaseIMView alloc] init];
    }
    return _baseIMView;
}

@end
