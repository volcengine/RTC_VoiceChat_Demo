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
package svc_service

import (
	"context"
	"time"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/application/voice_chat/svc_db"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/application/voice_chat/svc_entity"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/models/custom_error"
)

var userFactoryClient *UserFactory

type UserFactory struct {
	userRepo UserRepo
}

func GetUserFactory() *UserFactory {
	if userFactoryClient == nil {
		userFactoryClient = &UserFactory{
			userRepo: GetUserRepo(),
		}
	}
	return userFactoryClient
}

func (uf *UserFactory) NewUser(ctx context.Context, appID, roomID, userID, userName, deviceID string, userRole int) *User {
	dbUser := &svc_entity.SvcUser{
		AppID:          appID,
		RoomID:         roomID,
		UserID:         userID,
		UserName:       userName,
		UserRole:       userRole,
		NetStatus:      svc_db.UserNetStatusOffline,
		InteractStatus: svc_db.UserInteractStatusNormal,
		SeatID:         0,
		Mic:            1,
		CreateTime:     time.Now(),
		UpdateTime:     time.Now(),
		DeviceID:       deviceID,
	}
	user := &User{
		SvcUser: dbUser,
		isDirty: true,
	}
	return user
}

func (uf *UserFactory) Save(ctx context.Context, user *User) error {
	if user.IsDirty() {
		user.SetUpdateTime(time.Now())
		err := uf.userRepo.Save(ctx, user.SvcUser)
		if err != nil {
			return custom_error.InternalError(err)
		}
		user.SetIsDirty(false)
	}
	return nil
}

func (uf *UserFactory) GetActiveUserByRoomIDUserID(ctx context.Context, appID, roomID, userID string) (*User, error) {
	dbUser, err := uf.userRepo.GetActiveUserByRoomIDUserID(ctx, appID, roomID, userID)
	if err != nil {
		return nil, custom_error.InternalError(err)
	}
	if dbUser == nil {
		return nil, nil
	}
	user := &User{
		SvcUser: dbUser,
		isDirty: false,
	}
	return user, nil
}

func (uf *UserFactory) GetUserByRoomIDUserID(ctx context.Context, appID, roomID, userID string) (*User, error) {
	dbUser, err := uf.userRepo.GetUserByRoomIDUserID(ctx, appID, roomID, userID)
	if err != nil {
		return nil, custom_error.InternalError(err)
	}
	if dbUser == nil {
		return nil, nil
	}
	user := &User{
		SvcUser: dbUser,
		isDirty: false,
	}
	return user, nil
}

func (uf *UserFactory) GetActiveUserByUserID(ctx context.Context, appID, userID string) (*User, error) {
	dbUser, err := uf.userRepo.GetActiveUserByUserID(ctx, appID, userID)
	if err != nil {
		return nil, custom_error.InternalError(err)
	}
	if dbUser == nil {
		return nil, nil
	}
	user := &User{
		SvcUser: dbUser,
		isDirty: false,
	}
	return user, nil
}

func (uf *UserFactory) GetAudiencesWithoutApplyByRoomID(ctx context.Context, appID, roomID string) ([]*User, error) {
	dbAudiences, err := uf.userRepo.GetAudiencesWithoutApplyByRoomID(ctx, appID, roomID)
	if err != nil {
		return nil, custom_error.InternalError(err)
	}

	audiences := make([]*User, len(dbAudiences))
	for i := 0; i < len(dbAudiences); i++ {
		audiences[i] = &User{
			SvcUser: dbAudiences[i],
			isDirty: false,
		}
	}
	return audiences, nil
}

func (uf *UserFactory) GetApplyAudiencesByRoomID(ctx context.Context, appID, roomID string) ([]*User, error) {
	dbAudiences, err := uf.userRepo.GetApplyAudiencesByRoomID(ctx, appID, roomID)
	if err != nil {
		return nil, custom_error.InternalError(err)
	}

	audiences := make([]*User, len(dbAudiences))
	for i := 0; i < len(dbAudiences); i++ {
		audiences[i] = &User{
			SvcUser: dbAudiences[i],
			isDirty: false,
		}
	}
	return audiences, nil
}

func (uf *UserFactory) GetUsersByRoomID(ctx context.Context, appID, roomID string) ([]*User, error) {
	dbAudiences, err := uf.userRepo.GetActiveUsersByRoomID(ctx, appID, roomID)
	if err != nil {
		return nil, custom_error.InternalError(err)
	}

	audiences := make([]*User, len(dbAudiences))
	for i := 0; i < len(dbAudiences); i++ {
		audiences[i] = &User{
			SvcUser: dbAudiences[i],
			isDirty: false,
		}
	}
	return audiences, nil
}

func (uf *UserFactory) UpdateUsersByRoomID(ctx context.Context, appID, roomID string, ups map[string]interface{}) error {
	err := uf.userRepo.UpdateUsersWithMapByRoomID(ctx, appID, roomID, ups)
	if err != nil {
		return custom_error.InternalError(err)
	}
	return nil
}
