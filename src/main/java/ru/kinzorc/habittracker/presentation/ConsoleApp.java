package ru.kinzorc.habittracker.presentation;

import ru.kinzorc.habittracker.application.service.ApplicationService;
import ru.kinzorc.habittracker.core.repository.HabitRepository;
import ru.kinzorc.habittracker.core.repository.UserRepository;
import ru.kinzorc.habittracker.infrastructure.repository.jdbc.JdbcHabitRepository;
import ru.kinzorc.habittracker.infrastructure.repository.jdbc.JdbcUserRepository;
import ru.kinzorc.habittracker.infrastructure.repository.utils.JdbcConnector;
import ru.kinzorc.habittracker.presentation.menu.MenuNavigator;
import ru.kinzorc.habittracker.presentation.utils.MenuUtils;


public class ConsoleApp {
    public static void main(String[] args) {

        JdbcConnector jdbcConnector = new JdbcConnector();
        UserRepository userRepository = new JdbcUserRepository(jdbcConnector);
        HabitRepository habitRepository = new JdbcHabitRepository(jdbcConnector);
        ApplicationService applicationService = new ApplicationService(userRepository, habitRepository);
        MenuUtils menuUtils = new MenuUtils();

        MenuNavigator.MAIN_MENU.showMenu(applicationService, menuUtils);
    }
}
