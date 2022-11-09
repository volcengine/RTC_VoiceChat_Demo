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
package custom_error

import "errors"

var (
	ErrInput                       = NewCustomError(400, errors.New("input format error"))
	ErrUserIsActive                = NewCustomError(402, errors.New("user is active"))
	ErrUserIsInactive              = NewCustomError(404, errors.New("user is inactive"))
	ErrUserRoleNotMatch            = NewCustomError(405, errors.New("user role don't match"))
	ErrUserExceedLimit             = NewCustomError(406, errors.New("user exceed limit"))
	ErrUserTeacherInOtherClass     = NewCustomError(407, errors.New("teacher in other class"))
	ErrSameDeviceState             = NewCustomError(408, errors.New("same device state, no need to change"))
	ErrUserStudentInOtherClass     = NewCustomError(409, errors.New("student in other class"))
	ErrInvalidAppID                = NewCustomError(410, errors.New("app id is invalid"))
	ErrUserIsSharing               = NewCustomError(412, errors.New("user is sharing"))
	ErrUserInUse                   = NewCustomError(413, errors.New("user is in use"))
	ErrUserIsNotHost               = NewCustomError(416, errors.New("user is not host"))
	ErrUserIsNotOwner              = NewCustomError(417, errors.New("user is not room owner"))
	ErrCanNotReconnect             = NewCustomError(418, errors.New("can not reconnect"))
	ErrUserNotExist                = NewCustomError(419, errors.New("user not exist"))
	ErrRoomIsInactive              = NewCustomError(422, errors.New("room is inactive"))
	ErrRoomNotExist                = NewCustomError(422, errors.New("room not exist"))
	ErrHostNotAllowedLeave         = NewCustomError(420, errors.New("host is now allowed to leave meeting"))
	ErrMatchAntiDirt               = NewCustomError(430, errors.New("match anti dirt"))
	ErrorCodeExpiry                = NewCustomError(440, errors.New("verification code expired"))
	ErrorMessageCode               = NewCustomError(441, errors.New("verification code is incorrect"))
	ErrorTokenExpiry               = NewCustomError(450, errors.New("login token expired"))
	ErrorTokenEmpty                = NewCustomError(451, errors.New("login token empty"))
	ErrorTokenUserNotMatch         = NewCustomError(452, errors.New("login token user not mathc"))
	ErrorForbidHandsUp             = NewCustomError(461, errors.New("room forbid hands up"))
	ErrorDuplicateApproveMic       = NewCustomError(471, errors.New("duplicate approve mic on"))
	ErrorReachMicOnLimit           = NewCustomError(472, errors.New("reach mic on limit user count"))
	ErrorStudentNotHandsUp         = NewCustomError(473, errors.New("student is not hands up"))
	ErrorUserCantInviteForInviting = NewCustomError(481, errors.New("user can't be invited because who is inviting"))
	ErrorUserCantInviteForInteract = NewCustomError(482, errors.New("user can't be invited because who is interact"))
	ErrorCantInviteForFullInvite   = NewCustomError(483, errors.New("invite users count full"))
	ErrorCantInviteForFullInteract = NewCustomError(484, errors.New("interact users count full"))
	ErrTokenGenerate               = NewCustomError(702, errors.New("generate token error"))
	ErrUnknownConnectionState      = NewCustomError(500, errors.New("unknown connection state"))
	ErrLockRedis                   = NewCustomError(500, errors.New("lock redis room error"))
	ErrUnknownUserState            = NewCustomError(500, errors.New("unknown user state"))
	ErrMuteAll                     = NewCustomError(503, errors.New("mute all users error"))
	ErrChangeHost                  = NewCustomError(504, errors.New("change host error"))
	ErrUserOnMicExceedLimit        = NewCustomError(506, errors.New("user on mic exceed limit"))
	ErrRoomAlreadyExist            = NewCustomError(530, errors.New("room already exist"))
	ErrRequestSongRepeat           = NewCustomError(540, errors.New("request song repeat"))
	ErrRequestSongUserRoleNotMatch = NewCustomError(541, errors.New("user role don't match current song"))
)

type CustomError struct {
	err  error
	code int
}

func NewCustomError(code int, err error) *CustomError {
	return &CustomError{
		code: code,
		err:  err,
	}
}

func InternalError(err error) *CustomError {
	return &CustomError{
		code: 500,
		err:  err,
	}
}

func (e *CustomError) Error() string {
	return e.err.Error()
}

func (e *CustomError) Code() int {
	return e.code
}
