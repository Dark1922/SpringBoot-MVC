package curso.springboot.controller;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component //spring mapear pra gente
public class ReportUtil implements Serializable {
	
	private static final long serialVersionUID = 1L;

	//retorna nosso PDF em byte para dowload no navegador
	public byte[] geraRealatorio(List listDados, String relatorio,
			ServletContext servletContext) throws Exception{
		
		//fonte de dados de coleçõs de beans que são classes cria a lista de dados para o relatorio
		//com nossa lista de objetos para imprimir
		JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listDados);
		
		//Carregar o caminho do arquivo jasper compilado pasta relatorios  /barra pa direita
		//e p nome do relatorio e o nome final .jasper
		String caminhoJasper = servletContext.getRealPath("relatarios") + 
				File.separator + relatorio + ".jasper";
		
		//Carrega o arquivo jasper passando os dados
		JasperPrint impressoraJasper = JasperFillManager.
				fillReport(caminhoJasper, new HashedMap(), jrbcds);
		
		//vai exporta para bytes para fazer dowload do pdf
		return JasperExportManager.exportReportToPdf(impressoraJasper);
	}
}
	