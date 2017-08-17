/**
 * Copyright 2016 Antony Holmes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.columbia.rdf.matcalc;

import java.util.ArrayList;
import java.util.List;

import org.jebtk.core.PluginService;

import edu.columbia.rdf.matcalc.toolbox.Module;
import edu.columbia.rdf.matcalc.toolbox.core.ColumnAnnotationModule;
import edu.columbia.rdf.matcalc.toolbox.core.ExtractDataModule;
import edu.columbia.rdf.matcalc.toolbox.core.GroupModule;
import edu.columbia.rdf.matcalc.toolbox.core.SplitModule;
import edu.columbia.rdf.matcalc.toolbox.core.SummaryModule;
import edu.columbia.rdf.matcalc.toolbox.core.ViewModule;
import edu.columbia.rdf.matcalc.toolbox.core.ZoomModule;
import edu.columbia.rdf.matcalc.toolbox.core.collapse.CollapseModule;
import edu.columbia.rdf.matcalc.toolbox.core.duplicate.DuplicateModule;
import edu.columbia.rdf.matcalc.toolbox.core.filter.FilterModule;
import edu.columbia.rdf.matcalc.toolbox.core.filter.column.ColumnFilterModule;
import edu.columbia.rdf.matcalc.toolbox.core.filter.row.RowFilterModule;
import edu.columbia.rdf.matcalc.toolbox.core.io.CsvIOModule;
import edu.columbia.rdf.matcalc.toolbox.core.io.ExcelIOModule;
import edu.columbia.rdf.matcalc.toolbox.core.io.MatrixIOModule;
import edu.columbia.rdf.matcalc.toolbox.core.io.TsvIOModule;
import edu.columbia.rdf.matcalc.toolbox.core.io.TxtIOModule;
import edu.columbia.rdf.matcalc.toolbox.core.io.XlsIOModule;
import edu.columbia.rdf.matcalc.toolbox.core.io.XlsxIOModule;
import edu.columbia.rdf.matcalc.toolbox.core.match.MatchModule;
import edu.columbia.rdf.matcalc.toolbox.core.roworder.RowOrderModule;
import edu.columbia.rdf.matcalc.toolbox.core.search.SearchColumnModule;
import edu.columbia.rdf.matcalc.toolbox.core.shift.ShiftModule;
import edu.columbia.rdf.matcalc.toolbox.core.sort.SortColumnsByRowModule;
import edu.columbia.rdf.matcalc.toolbox.core.sort.SortModule;
import edu.columbia.rdf.matcalc.toolbox.core.venn.VennModule;
import edu.columbia.rdf.matcalc.toolbox.math.LogModule;
import edu.columbia.rdf.matcalc.toolbox.math.NormalizeModule;
import edu.columbia.rdf.matcalc.toolbox.math.PowerModule;
import edu.columbia.rdf.matcalc.toolbox.math.QuantileNormalizeModule;
import edu.columbia.rdf.matcalc.toolbox.math.TransposeModule;
import edu.columbia.rdf.matcalc.toolbox.math.ZScoreModule;
import edu.columbia.rdf.matcalc.toolbox.plot.barchart.BarChartModule;
import edu.columbia.rdf.matcalc.toolbox.plot.barchart.HistogramModule;
import edu.columbia.rdf.matcalc.toolbox.plot.barchart.StackedBarChartModule;
import edu.columbia.rdf.matcalc.toolbox.plot.boxwhisker.BoxWhiskerPlotModule;
import edu.columbia.rdf.matcalc.toolbox.plot.boxwhisker.BoxWhiskerScatterPlotModule;
import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.cluster.legacy.LegacyClusterModule;
import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.legacy.LegacyHeatMapModule;
import edu.columbia.rdf.matcalc.toolbox.plot.scatter.ScatterModule;
import edu.columbia.rdf.matcalc.toolbox.plot.scatter.SmoothedLineGraphModule;
import edu.columbia.rdf.matcalc.toolbox.plot.volcano.VolcanoPlotModule;
import edu.columbia.rdf.matcalc.toolbox.stats.ttest.legacy.TTestModule;

// TODO: Auto-generated Javadoc
/**
 * The Class ModuleLoader.
 */
public class ModuleLoader {
	
	/** The m modules list. */
	private List<Class<? extends Module>> mModulesList = 
			new ArrayList<Class<? extends Module>>();
	
	/**
	 * Instantiates a new module loader.
	 */
	public ModuleLoader() {
		addModule(MatrixIOModule.class);
		addModule(ExcelIOModule.class);
		addModule(XlsxIOModule.class);
		addModule(XlsIOModule.class);
		addModule(TxtIOModule.class);
		addModule(TsvIOModule.class);
		addModule(CsvIOModule.class);
		addModule(LegacyHeatMapModule.class);
		addModule(LegacyClusterModule.class);
		addModule(SmoothedLineGraphModule.class);
		//addModule(ScatterLineModule.class);
		addModule(ScatterModule.class);
		addModule(BarChartModule.class);
		//addModule(BarChartHModule.class);
		addModule(StackedBarChartModule.class);
		addModule(HistogramModule.class);
		//addModule(PieChartModule.class);
		addModule(BoxWhiskerPlotModule.class);
		addModule(BoxWhiskerScatterPlotModule.class);
		addModule(VolcanoPlotModule.class);
		addModule(ShiftModule.class);
		
		addModule(TransposeModule.class);
		addModule(LogModule.class);
		addModule(PowerModule.class);
		addModule(ZScoreModule.class);
		addModule(NormalizeModule.class);
		addModule(QuantileNormalizeModule.class);
		
		addModule(SortModule.class);
		addModule(SortColumnsByRowModule.class);
		addModule(FilterModule.class);
		addModule(RowFilterModule.class);
		addModule(ColumnFilterModule.class);
		addModule(SearchColumnModule.class);
		addModule(MatchModule.class);
		
		
		addModule(SummaryModule.class);
		addModule(CollapseModule.class);
		addModule(DuplicateModule.class);
		addModule(SplitModule.class);
		addModule(ExtractDataModule.class);
		addModule(GroupModule.class);
		addModule(ColumnAnnotationModule.class);
		
		addModule(RowOrderModule.class);
		addModule(ColumnFilterModule.class);
		addModule(TTestModule.class);
		addModule(VennModule.class);
		addModule(ZoomModule.class);
		addModule(ViewModule.class);
		
	}
	
	/**
	 * Adds the module.
	 *
	 * @param c the c
	 * @return the module loader
	 */
	public ModuleLoader addModule(Class<? extends Module> c) {
		//PluginService.getInstance().addPlugin(c);
		
		mModulesList.add(c);
		
		return this;
	}
	
	/**
	 * Load modules.
	 */
	public void loadModules() {
		for (Class<?> c : mModulesList) {
			PluginService.getInstance().addPlugin(c);
		}
	}
}
