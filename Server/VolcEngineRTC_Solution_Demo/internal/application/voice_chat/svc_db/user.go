/*
 * Copyright 2022 CloudWeGo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package svc_db

import (
	"context"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/util"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/application/voice_chat/svc_entity"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/db"
	"gorm.io/gorm"
	"gorm.io/gorm/clause"
)

const UserTable = "svc_user"

const (
	UserRoleHost     = 1
	UserRoleAudience = 2
)
const (
	UserNetStatusOnline       = 1
	UserNetStatusOffline      = 2
	UserNetStatusReconnecting = 3
)

const (
	UserInteractStatusNormal      = 1
	UserInteractStatusInteracting = 2
	UserInteractStatusApplying    = 3
	UserInteractStatusInviting    = 4
)

type DbUserRepo struct{}

func (repo *DbUserRepo) Save(ctx context.Context, user *svc_entity.SvcUser) error {
	defer util.CheckPanic()
	err := db.Client.WithContext(ctx).Debug().Table(UserTable).
		Clauses(clause.OnConflict{
			Columns:   []clause.Column{{Name: "app_id"}, {Name: "room_id"}, {Name: "user_id"}},
			UpdateAll: true,
		}).Create(&user).Error
	return err
}

func (repo *DbUserRepo) UpdateUsersWithMapByRoomID(ctx context.Context, appID, roomID string, ups map[string]interface{}) error {
	defer util.CheckPanic()
	return db.Client.WithContext(ctx).Debug().Table(UserTable).Where("app_id =? and room_id = ?", appID, roomID).Updates(ups).Error
}

func (repo *DbUserRepo) GetActiveUserByRoomIDUserID(ctx context.Context, appID, roomID, userID string) (*svc_entity.SvcUser, error) {
	defer util.CheckPanic()
	var rs *svc_entity.SvcUser
	err := db.Client.WithContext(ctx).Debug().Table(UserTable).Where("app_id=? and room_id = ? and user_id = ? and net_status in ?", appID, roomID, userID, []int{UserNetStatusOnline, UserNetStatusReconnecting}).First(&rs).Error
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, nil
		}
		return nil, err
	}
	return rs, nil
}

func (repo *DbUserRepo) GetUserByRoomIDUserID(ctx context.Context, appID, roomID, userID string) (*svc_entity.SvcUser, error) {
	defer util.CheckPanic()
	var rs *svc_entity.SvcUser
	err := db.Client.WithContext(ctx).Debug().Table(UserTable).Where("app_id=? and room_id = ? and user_id = ?", appID, roomID, userID).First(&rs).Error
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, nil
		}
		return nil, err
	}
	return rs, nil
}

func (repo *DbUserRepo) GetActiveUserByUserID(ctx context.Context, appID, userID string) (*svc_entity.SvcUser, error) {
	defer util.CheckPanic()
	var rs *svc_entity.SvcUser
	err := db.Client.WithContext(ctx).Debug().Table(UserTable).Where("app_id=? and user_id = ? and net_status in ?", appID, userID, []int{UserNetStatusOnline, UserNetStatusReconnecting}).First(&rs).Error
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, nil
		}
		return nil, err
	}
	return rs, nil
}

func (repo *DbUserRepo) GetActiveUsersByRoomID(ctx context.Context, appID, roomID string) ([]*svc_entity.SvcUser, error) {
	defer util.CheckPanic()
	var rs []*svc_entity.SvcUser
	err := db.Client.WithContext(ctx).Debug().Table(UserTable).Where("app_id=? and room_id = ? and net_status = ?", appID, roomID, UserNetStatusOnline).Order("create_time desc").Find(&rs).Error
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, nil
		}
		return nil, err
	}
	return rs, nil
}

func (repo *DbUserRepo) GetAudiencesWithoutApplyByRoomID(ctx context.Context, appID, roomID string) ([]*svc_entity.SvcUser, error) {
	defer util.CheckPanic()
	var rs []*svc_entity.SvcUser
	err := db.Client.WithContext(ctx).Debug().Table(UserTable).Where("app_id=? and room_id = ? and net_status = ? and interact_status <> ? and user_role = ?", appID, roomID, UserNetStatusOnline, UserInteractStatusApplying, UserRoleAudience).Order("create_time desc").Limit(200).Find(&rs).Error
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, nil
		}
		return nil, err
	}
	return rs, nil
}

func (repo *DbUserRepo) GetApplyAudiencesByRoomID(ctx context.Context, appID, roomID string) ([]*svc_entity.SvcUser, error) {
	defer util.CheckPanic()
	var rs []*svc_entity.SvcUser
	err := db.Client.WithContext(ctx).Debug().Table(UserTable).Where("app_id=? and room_id= ? and net_status = ? and interact_status = ? and user_role = ?", appID, roomID, UserNetStatusOnline, UserInteractStatusApplying, UserRoleAudience).Order("create_time desc").Limit(200).Find(&rs).Error
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, nil
		}
		return nil, err
	}
	return rs, nil
}
