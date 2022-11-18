//
//  VoiceChatRoomTableView.m
//  veRTC_Demo
//
//  Created by on 2021/5/18.
//  
//

#import "VoiceChatRoomTableView.h"
#import "VoiceChatEmptyView.h"

@interface VoiceChatRoomTableView () <UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, strong) UITableView *roomTableView;
@property (nonatomic, strong) VoiceChatEmptyView *emptyComponent;

@end


@implementation VoiceChatRoomTableView

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
        [self.emptyComponent show];
    } else {
        [self.emptyComponent dismiss];
    }
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    VoiceChatRoomCell *cell = [tableView dequeueReusableCellWithIdentifier:@"VoiceChatRoomCellID" forIndexPath:indexPath];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.model = self.dataLists[indexPath.row];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:NO];

    if ([self.delegate respondsToSelector:@selector(VoiceChatRoomTableView:didSelectRowAtIndexPath:)]) {
        [self.delegate VoiceChatRoomTableView:self didSelectRowAtIndexPath:self.dataLists[indexPath.row]];
    }
}

#pragma mark - UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataLists.count;
}


#pragma mark - getter


- (UITableView *)roomTableView {
    if (!_roomTableView) {
        _roomTableView = [[UITableView alloc] init];
        _roomTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        _roomTableView.delegate = self;
        _roomTableView.dataSource = self;
        [_roomTableView registerClass:VoiceChatRoomCell.class forCellReuseIdentifier:@"VoiceChatRoomCellID"];
        _roomTableView.backgroundColor = [UIColor clearColor];
        _roomTableView.rowHeight = UITableViewAutomaticDimension;
        _roomTableView.estimatedRowHeight = 139;
    }
    return _roomTableView;
}

- (VoiceChatEmptyView *)emptyComponent {
    if (!_emptyComponent) {
        _emptyComponent = [[VoiceChatEmptyView alloc] initWithView:self
                                                                  message:@"还没有人创建聊天室,快去创建吧"];
    }
    return _emptyComponent;
}

@end
