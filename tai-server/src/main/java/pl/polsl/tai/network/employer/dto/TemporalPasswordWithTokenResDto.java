package pl.polsl.tai.network.employer.dto;

public record TemporalPasswordWithTokenResDto(String token, long expiredSeconds, String temporalPassword) {
}
