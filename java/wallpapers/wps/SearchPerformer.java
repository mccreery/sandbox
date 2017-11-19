package wps;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import wps.swing.WallpaperFinder;

public class SearchPerformer implements ActionListener {
	private final WallpaperFinder frame;

	public SearchPerformer(WallpaperFinder frame) {
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.frame.searchTerms.getText().trim().isEmpty()) {
			Util.error(this.frame, "Search terms cannot be empty.");
			return;
		}

		Desktop desktop;
		if(!Desktop.isDesktopSupported() || !(desktop = Desktop.getDesktop()).isSupported(Action.BROWSE)) {
			Util.error(this.frame, "Your operating system does not support opening links.");
			return;
		}

		StringBuilder url = new StringBuilder();
		try {
			url.append("http://www.google.com/search?q=").append(URLEncoder.encode(this.frame.searchTerms.getText(), "UTF8"));
			url.append("&tbm=isch&tbs=isz:ex,iszw:").append(frame.getWidth()).append(",iszh:").append(frame.getHeight());

			if(this.frame.onlyJPG.isSelected()) {
				url.append("&as_filetype=jpg");
			} else if(this.frame.onlyPNG.isSelected()) {
				url.append("&as_filetype=png");
			}
			desktop.browse(URI.create(url.toString()));
		} catch(UnsupportedEncodingException ex) {
			Util.error(this.frame, "Your Java implementation does not support UTF-8.\nSearch terms cannot be encoded.");
			ex.printStackTrace();
		} catch(IOException ex) {
			Util.error(this.frame, "An unknown network error occurred.");
			ex.printStackTrace();
		}
	}
}
