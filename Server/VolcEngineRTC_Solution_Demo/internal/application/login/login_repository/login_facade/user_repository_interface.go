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
package login_facade

import (
	"context"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/application/login/login_entity"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/application/login/login_repository/login_implement"
)

var userRepository UserRepositoryInterface

func GetUserRepository() UserRepositoryInterface {
	if userRepository == nil {
		userRepository = &login_implement.UserRepositoryImpl{}
	}
	return userRepository
}

type UserRepositoryInterface interface {
	Save(ctx context.Context, user *login_entity.UserProfile) error
	GetUser(ctx context.Context, userID string) (*login_entity.UserProfile, error)
}
