import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IActionKey, ActionKey } from '../action-key.model';
import { ActionKeyService } from '../service/action-key.service';

@Component({
  selector: 'jhi-action-key-update',
  templateUrl: './action-key-update.component.html',
})
export class ActionKeyUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    action: [],
    keys: [],
  });

  constructor(protected actionKeyService: ActionKeyService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ actionKey }) => {
      this.updateForm(actionKey);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const actionKey = this.createFromForm();
    if (actionKey.id !== undefined) {
      this.subscribeToSaveResponse(this.actionKeyService.update(actionKey));
    } else {
      this.subscribeToSaveResponse(this.actionKeyService.create(actionKey));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IActionKey>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(actionKey: IActionKey): void {
    this.editForm.patchValue({
      id: actionKey.id,
      action: actionKey.action,
      keys: actionKey.keys,
    });
  }

  protected createFromForm(): IActionKey {
    return {
      ...new ActionKey(),
      id: this.editForm.get(['id'])!.value,
      action: this.editForm.get(['action'])!.value,
      keys: this.editForm.get(['keys'])!.value,
    };
  }
}
