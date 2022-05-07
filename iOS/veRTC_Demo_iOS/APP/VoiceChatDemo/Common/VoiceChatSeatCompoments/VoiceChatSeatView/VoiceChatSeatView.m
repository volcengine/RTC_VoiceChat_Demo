//
//  VoiceChatSeatView.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/11/29.
//  Copyright © 2021 . All rights reserved.
//

#import "VoiceChatSeatView.h"
#import "VoiceChatSeatItemView.h"
#import "GCDTimer.h"

static const NSInteger MaxNumber = 8;

@interface VoiceChatSeatView ()

@property (nonatomic, strong) NSMutableArray<VoiceChatSeatItemView *> *itemViewLists;
@property (nonatomic, strong) GCDTimer *timer;
@property (nonatomic, copy) NSDictionary *volumeDic;
@end

@implementation VoiceChatSeatView

- (instancetype)init {
    self = [super init];
    if (self) {
        [self addSubviewAndConstraints];
        __weak __typeof(self) wself = self;
        [self.timer startTimerWithSpace:0.6 block:^(BOOL result) {
            [wself timerMethod];
        }];
    }
    return self;
}

- (void)setSeatList:(NSArray<VoiceChatSeatModel *> *)seatList {
    _seatList = seatList;
    
    for (int i = 0; i < self.itemViewLists.count; i++) {
        VoiceChatSeatItemView *itemView = self.itemViewLists[i];
        if (i < seatList.count) {
            itemView.seatModel = seatList[i];
        } else {
            itemView.seatModel = nil;
        }
    }
}

- (void)addSeatModel:(VoiceChatSeatModel *)seatModel {
    VoiceChatSeatItemView *selectItemView = nil;
    for (VoiceChatSeatItemView *itemView in self.itemViewLists) {
        if (itemView.index == seatModel.index) {
            selectItemView = itemView;
            break;
        }
    }
    if (selectItemView) {
        selectItemView.seatModel = seatModel;
    }
}

- (void)removeUserModel:(VoiceChatUserModel *)userModel {
    VoiceChatSeatItemView *deleteItemView = nil;
    for (VoiceChatSeatItemView *itemView in self.itemViewLists) {
        if ([itemView.seatModel.userModel.uid isEqualToString:userModel.uid]) {
            deleteItemView = itemView;
            break;
        }
    }
    if (deleteItemView) {
        VoiceChatSeatModel *seatModel = deleteItemView.seatModel;
        seatModel.userModel = nil;
        deleteItemView.seatModel = seatModel;
    }
}

- (void)updateSeatModel:(VoiceChatSeatModel *)seatModel {
    VoiceChatSeatItemView *updateItemView = nil;
    for (VoiceChatSeatItemView *itemView in self.itemViewLists) {
        if (itemView.index == seatModel.index) {
            updateItemView = itemView;
            break;
        }
    }
    if (updateItemView) {
        updateItemView.seatModel = seatModel;
    }
}

- (void)updateSeatVolume:(NSDictionary *)volumeDic {
    _volumeDic = volumeDic;
}

#pragma mark - Private Action

- (void)timerMethod {
    if (_volumeDic.count > 0) {
        for (VoiceChatSeatItemView *itemView in self.itemViewLists) {
            VoiceChatSeatModel *tempSeatModel = itemView.seatModel;
            NSNumber *volumeValue = _volumeDic[tempSeatModel.userModel.uid];
            if (NOEmptyStr(tempSeatModel.userModel.uid) &&
                volumeValue.floatValue > 0) {
                tempSeatModel.userModel.volume = volumeValue.floatValue;
            } else {
                tempSeatModel.userModel.volume = 0;
            }
            itemView.seatModel = tempSeatModel;
        }
    }
}

- (void)addSubviewAndConstraints {
    NSMutableArray *line1List = [[NSMutableArray alloc] init];
    for (int i = 0; i < MaxNumber / 2; i++) {
        VoiceChatSeatItemView *itemView = [[VoiceChatSeatItemView alloc] init];
        itemView.index = i + 1;
        [self addSubview:itemView];
        [line1List addObject:itemView];
        [self.itemViewLists addObject:itemView];
        
        __weak __typeof(self) wself = self;
        itemView.clickBlock = ^(VoiceChatSeatModel *seatModel) {
            if (wself.clickBlock) {
                wself.clickBlock(seatModel);
            }
        };
    }
    [line1List mas_distributeViewsAlongAxis:MASAxisTypeHorizontal
                                 withFixedItemLength:52
                                         leadSpacing:0
                                         tailSpacing:0];
    [line1List mas_updateConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self);
        make.height.mas_equalTo(80);
    }];
    
    NSMutableArray *line2List = [[NSMutableArray alloc] init];
    for (int i = 0; i < MaxNumber / 2; i++) {
        VoiceChatSeatItemView *itemView = [[VoiceChatSeatItemView alloc] init];
        itemView.index = i + 1 + (MaxNumber / 2);
        [self addSubview:itemView];
        [line2List addObject:itemView];
        [self.itemViewLists addObject:itemView];
        
        __weak __typeof(self) wself = self;
        itemView.clickBlock = ^(VoiceChatSeatModel *seatModel) {
            if (wself.clickBlock) {
                wself.clickBlock(seatModel);
            }
        };
    }
    [line2List mas_distributeViewsAlongAxis:MASAxisTypeHorizontal
                                 withFixedItemLength:52
                                         leadSpacing:0
                                         tailSpacing:0];
    [line2List mas_updateConstraints:^(MASConstraintMaker *make) {
        make.bottom.equalTo(self);
        make.height.mas_equalTo(80);
    }];
}

#pragma mark - Getter

- (NSMutableArray<VoiceChatSeatItemView *> *)itemViewLists {
    if (!_itemViewLists) {
        _itemViewLists = [[NSMutableArray alloc] init];
    }
    return _itemViewLists;
}

- (GCDTimer *)timer {
    if (!_timer) {
        _timer = [[GCDTimer alloc] init];
    }
    return _timer;
}

@end
