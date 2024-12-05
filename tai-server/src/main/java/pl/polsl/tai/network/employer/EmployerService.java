package pl.polsl.tai.network.employer;

import pl.polsl.tai.domain.user.UserEntity;
import pl.polsl.tai.dto.PageableContainerResDto;
import pl.polsl.tai.network.employer.dto.*;
import pl.polsl.tai.security.LoggedUser;

interface EmployerService {
	PageableContainerResDto<EmployerRowResDto, UserEntity> getPageableEmployers(Integer page, Integer size);

	TemporalPasswordWithTokenResDto createEmployer(AddEmployerReqDto reqDto, LoggedUser loggedUser);

	UpdateEmployerResDto updateEmployer(Long id, UpdateEmployerReqDto reqDto, LoggedUser loggedUser);

	TemporalPasswordWithTokenResDto firstAccessRegenerateToken(Long id, LoggedUser loggedUser);

	void firstAccessUpdatePassword(String token, FirstAccessUpdatePasswordReqDto reqDto);

	void deleteEmployer(Long id, LoggedUser loggedUser);
}
