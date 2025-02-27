package pl.polsl.tai.log;

public interface LogPersistService {
  void info(String message, Object... args);

  void error(String message, Object... args);
}
