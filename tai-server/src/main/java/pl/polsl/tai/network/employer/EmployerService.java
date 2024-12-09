package pl.polsl.tai.network.employer;

import pl.polsl.tai.domain.user.UserEntity;
import pl.polsl.tai.dto.DeleteResultResDto;
import pl.polsl.tai.dto.PageableResDto;
import pl.polsl.tai.network.employer.dto.*;
import pl.polsl.tai.security.LoggedUser;

import java.util.List;

interface EmployerService {
	PageableResDto<EmployerRowResDto, UserEntity> getPageableEmployers(String email, Integer page, Integer size);

	TemporalPasswordWithTokenResDto createEmployer(AddEmployerReqDto reqDto, LoggedUser loggedUser);

	UpdateEmployerResDto updateEmployer(Long employerId, UpdateEmployerReqDto reqDto, LoggedUser loggedUser);

	TemporalPasswordWithTokenResDto firstAccessRegenerateToken(Long employerId, LoggedUser loggedUser);

	void firstAccessUpdatePassword(String token, FirstAccessUpdatePasswordReqDto reqDto);

	DeleteResultResDto deleteEmployers(List<Long> employerIds, LoggedUser loggedUser);
}
