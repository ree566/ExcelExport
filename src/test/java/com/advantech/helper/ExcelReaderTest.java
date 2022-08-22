/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.model.db1.Floor;
import com.advantech.model.db1.ScrappedDetail;
import com.advantech.model.db1.Unit;
import com.advantech.model.db1.User;
import com.advantech.model.db1.UserProfile;
import com.advantech.repo.db1.FloorRepository;
import com.advantech.repo.db1.UnitRepository;
import com.advantech.repo.db1.UserProfileRepository;
import com.advantech.repo.db1.UserRepository;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class ExcelReaderTest {

    @Autowired
    private ExcelDataTransformer t;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private FloorRepository floorRepo;

    @Autowired
    private UserProfileRepository profileRepo;

    @Autowired
    private UnitRepository unitRepo;

//    @Test
    public void testRead() throws Exception {
        List<ScrappedDetail> list1 = t.getFloorFiveExcelData();
        assertTrue(!list1.isEmpty());

        List<String> model = list1.stream().map(ScrappedDetail::getMaterialNumber).collect(toList());

        model.forEach(s -> {
            if (s.contains(".")) {
                System.out.println(Long.toString(new BigDecimal(s).longValue()));
            } else {
                System.out.println(s);
            }
        });

    }

    @Test
    @Transactional
    @Rollback(false)
    public void testImportUser() throws Exception {
        List<User> users = userRepo.findAll();
        List<UserProfile> profiles = profileRepo.findAll();
        List<Floor> floors = floorRepo.findAll();
        CustomPasswordEncoder encoder = new CustomPasswordEncoder();
        Unit unit = unitRepo.getOne(1);
        UserProfile userRole = profileRepo.getOne(3);
        UserProfile operRole = profileRepo.getOne(5);

        DataFormatter dataFormatter = new DataFormatter();
        String fileLocation = "C:\\Users\\wei.cheng\\Desktop\\開發快速領料平台介面同仁名單.xlsx";

        try (Workbook workbook = WorkbookFactory.create(new File(fileLocation))) {
            Sheet sheet = workbook.getSheetAt(0);
            sheet.forEach(row -> {
                String jobnumber = dataFormatter.formatCellValue(row.getCell(1));
                String name = dataFormatter.formatCellValue(row.getCell(2));
                String floorName = dataFormatter.formatCellValue(row.getCell(3));
                String userRoleString = dataFormatter.formatCellValue(row.getCell(4));
//                System.out.printf("%s\t%s\t%s\t%s\t\r\n", jobnumber, name, floorName, userRole);

                User user = users.stream().filter(u -> u.getJobnumber().equals(jobnumber.trim())).findFirst().orElse(null);
                if (!jobnumber.trim().equals("") && user == null && !name.equals("姓名")) {
                    user = new User();
                    user.setJobnumber(jobnumber);
                    user.setUsername(name);
                    user.setPassword(encoder.encode(jobnumber));
                    user.setEmail("");
                    user.setFloor(floors.stream().filter(f -> f.getName().equals(floorName)).findFirst().orElse(null));
                    user.setUnit(unit);
                    userRepo.save(user);
                    Set roles = new HashSet();
                    roles.add(userRoleString.equals("使用者") ? userRole : operRole);
                    user.setUserProfiles(roles);
                    userRepo.save(user);
                }

            });
            // Closing the workbook
        }
    }
}
