package com.ticketingsystem.http;

import android.content.Context;
import android.support.v4.view.ViewPager;

import com.ticketingsystem.navigation.NavigationService;

public class HttpClient {

    private String serverUrl;
    private Context context;

    public HttpClient(Context context, String serverUrl) {
        this.context = context;
        this.serverUrl = serverUrl;
    }
/*
    public void Register(String username, String password, String confirmPassword, ViewPager viewPager) {
        String registerUrl = String.format("%sapi/account/register", this.serverUrl);
        new RegisterAsync(context, viewPager, registerUrl).execute(username, password, confirmPassword);
    }
*/
    public void Login(String username, String password, NavigationService navigationService) {
        String loginUrl = String.format("%s/token", this.serverUrl);
        new LoginAsync(context, navigationService, loginUrl).execute(username, password);

    }
/*
    public void UploadImage(String filePath, int issueId) {
        TokensDbHandler tokensDbHandler = new TokensDbHandler(this.context, null);
        StringBuilder url = new StringBuilder();
        url.append(this.serverUrl);
        url.append("/");
        url.append(String.valueOf(issueId));

        ImageUpload imageUpload = new ImageUpload(url, "http", filePath, tokensDbHandler.getToken("login"));
        imageUpload.upload();
    }

    public void LoadTopVotedIssues(ITopVotedResult topVotedResult, int count, ListItemAdapter adapter) {
        TokensDbHandler tokensDbHandler = new TokensDbHandler(this.context, null);
        String loadTopVotedIssuesUrl = String.format("%sapi/issues/sortedbyvotes/%d", this.serverUrl, count);
        new LoadTopVotedIssuesAsync(this.context, topVotedResult, tokensDbHandler.getToken("login"), loadTopVotedIssuesUrl, this.serverUrl, adapter).execute();
    }

    public void LoadMyIssues(IMyIssue myIssues, ListItemAdapter adapter) {
        TokensDbHandler tokensDbHandler = new TokensDbHandler(this.context, null);
        String loadMyIssuesUrl = String.format("%sapi/issues/my", this.serverUrl);
        new LoadMyIssuesAsync(this.context, myIssues, tokensDbHandler.getToken("login"), loadMyIssuesUrl, serverUrl, adapter).execute();
    }

    public void LoadSearchIssues(ISearchResult issues, SearchIssueData searchIssueData, ListItemAdapter adapter) {
        TokensDbHandler tokensDbHandler = new TokensDbHandler(this.context, null);
        String loadIssuesUrl = String.format("%sapi/issues/search/%s/%s", this.serverUrl, searchIssueData.getCity(), searchIssueData.getTitle());
        new SearchIssueAsync(this.context, tokensDbHandler.getToken("login"), loadIssuesUrl, this.serverUrl, issues, adapter).execute();
    }

    public void UpVote(View view, int issueId) {
        TokensDbHandler tokensDbHandler = new TokensDbHandler(this.context, null);
        String upVoteIssueUrl = String.format("%sapi/issues/voteup/%d", this.serverUrl, issueId);
        new UpVoteAsync(this.context, upVoteIssueUrl, tokensDbHandler.getToken("login"), view).execute();
    }

    public void ReportIncorrectIssue(int issueId) {
        TokensDbHandler tokensDbHandler = new TokensDbHandler(this.context, null);
        String reportIssueUrl = String.format("%sapi/issues/report/%d", this.serverUrl, issueId);
        new ReportAsync(this.context, reportIssueUrl, tokensDbHandler.getToken("login")).execute();
    }
    */
}