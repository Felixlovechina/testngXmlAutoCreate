package org.testng;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.testng.util.AutoCreatXML_VirtualFIle;


/**
 * Created by felix on 17/3/10.
 */
public class CreateTestNgXMLAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        getfilessss(e);
//        getannatation(e);


    }


    private void getfilessss(AnActionEvent e) {

        Project mProject = e.getData(PlatformDataKeys.PROJECT);
        System.out.println("mProject.getBasePath(); = " + mProject.getBasePath());
        DataContext dataContext = e.getDataContext();

        VirtualFile[] files = DataKeys.VIRTUAL_FILE_ARRAY.getData(dataContext);
        String re = AutoCreatXML_VirtualFIle.getInstance().readVirtualFile(files,mProject.getBasePath());


        Messages.showMessageDialog(mProject, "Hi, process result : \n"+re+"\n\n Check Please .", "结果", Messages.getInformationIcon());
    }




}
