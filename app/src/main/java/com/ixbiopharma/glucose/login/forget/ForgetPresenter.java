package com.ixbiopharma.glucose.login.forget;

import com.ixbiopharma.glucose.repository.UserRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * ForgetPresenter
 * <p>
 * Created by ivan on 14.05.17.
 */

class ForgetPresenter implements ForgetContract.Presenter {

    private ForgetContract.View view;
    private UserRepository userRepository;

    ForgetPresenter(ForgetContract.View view, UserRepository userRepository) {
        this.view = view;
        this.userRepository = userRepository;
    }

    @Override
    public void forget() {
        String email = view.getEmailField();

        if (email.isEmpty()) {
            view.onError("Email cant be empty!");
            return;
        }

        userRepository.forgetPassword(email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(response -> {
                    if (response.getStatus() != 1) {
                        view.onError(response.getMessage());
                    } else {
                        view.showCode();
                    }
                }, throwable -> {
                    view.onError(throwable.getMessage());
                    throwable.printStackTrace();
                });
    }

    @Override
    public void enterCode(String code, String password) {
        userRepository.emailCode(code, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(() -> view.onForgetSuccess(), throwable -> {
                    view.onError(throwable.getMessage());
                    throwable.printStackTrace();
                });
    }
}
