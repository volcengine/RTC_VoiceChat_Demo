//
//  VoiceChatRoomUserListView.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/5/18.
//  Copyright © 2021 . All rights reserved.
//

#import "VoiceChatRoomAudienceListsView.h"
#import "VoiceChatEmptyView.h"
#import "VoiceChatRTMManager.h"

@interface VoiceChatRoomAudienceListsView ()<UITableViewDelegate, UITableViewDataSource, VoiceChatRoomUserListtCellDelegate>

@property (nonatomic, strong) UITableView *roomTableView;
@property (nonatomic, strong) VoiceChatEmptyView *emptyCompoments;

@end


@implementation VoiceChatRoomAudienceListsView


- (instancetype)init {
    self = [super init];
    if (self) {
        [self addSubview:self.roomTableView];
        [self.roomTableView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.equalTo(self);
        }];
    }
    return self;
}

#pragma mark - Publish Action

- (void)setDataLists:(NSArray *)dataLists {
    _dataLists = dataLists;
    
    [self.roomTableView reloadData];
    if (dataLists.count <= 0) {
        [self.emptyCompoments show];
    } else {
        [self.emptyCompoments dismiss];
    }
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    VoiceChatRoomUserListtCell *cell = [tableView dequeueReusableCellWithIdentifier:@"VoiceChatRoomUserListtCellID" forIndexPath:indexPath];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.model = (VoiceChatUserModel *)self.dataLists[indexPath.row];
    cell.delegate = self;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:NO];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 60;
}

#pragma mark - UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataLists.count;
}

#pragma mark - VoiceChatRoomUserListtCellDelegate

- (void)VoiceChatRoomUserListtCell:(VoiceChatRoomUserListtCell *)VoiceChatRoomUserListtCell clickButton:(id)model {
    if ([self.delegate respondsToSelector:@selector(voiceChatRoomAudienceListsView:clickButton:)]) {
        [self.delegate voiceChatRoomAudienceListsView:self clickButton:model];
    }
}

#pragma mark - getter

- (UITableView *)roomTableView {
    if (!_roomTableView) {
        _roomTableView = [[UITableView alloc] init];
        _roomTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        _roomTableView.delegate = self;
        _roomTableView.dataSource = self;
        [_roomTableView registerClass:VoiceChatRoomUserListtCell.class forCellReuseIdentifier:@"VoiceChatRoomUserListtCellID"];
        _roomTableView.backgroundColor = [UIColor clearColor];
    }
    return _roomTableView;
}

- (VoiceChatEmptyView *)emptyCompoments {
    if (!_emptyCompoments) {
        _emptyCompoments = [[VoiceChatEmptyView alloc] initWithView:self
                                                                  message:@"暂无在线观众"];
    }
    return _emptyCompoments;
}

- (void)dealloc {
    NSLog(@"dealloc %@",NSStringFromClass([self class]));
}

@end
