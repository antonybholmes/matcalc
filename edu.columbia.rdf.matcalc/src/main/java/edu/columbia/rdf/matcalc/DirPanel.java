package edu.columbia.rdf.matcalc;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.io.ModernFileCrumb;
import org.jebtk.modern.io.PwdModel;
import org.jebtk.modern.io.RecentFilesService;

public class DirPanel extends ModernComponent {
	private static final long serialVersionUID = 1L;

	private ModernFileCrumb mFileCrumb = 
			new ModernFileCrumb(RecentFilesService.getInstance().getPwd());


	private PwdModel mModel;
	
	public DirPanel(PwdModel model) {
		mModel = model;
		
		model.addChangeListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent e) {
				mFileCrumb.updateDir(mModel.getPwd());
			}});
		
		mFileCrumb = new ModernFileCrumb(model.getPwd());
		
		mFileCrumb.addChangeListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent e) {
				mModel.setPwd(mFileCrumb.getDir());
			}});
		
		setBody(mFileCrumb);
		
		setBorder(DOUBLE_BORDER);
	}
}
