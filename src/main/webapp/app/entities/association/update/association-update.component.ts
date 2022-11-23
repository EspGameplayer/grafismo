import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAssociation, Association } from '../association.model';
import { AssociationService } from '../service/association.service';

@Component({
  selector: 'jhi-association-update',
  templateUrl: './association-update.component.html',
})
export class AssociationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
  });

  constructor(protected associationService: AssociationService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ association }) => {
      this.updateForm(association);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const association = this.createFromForm();
    if (association.id !== undefined) {
      this.subscribeToSaveResponse(this.associationService.update(association));
    } else {
      this.subscribeToSaveResponse(this.associationService.create(association));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssociation>>): void {
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

  protected updateForm(association: IAssociation): void {
    this.editForm.patchValue({
      id: association.id,
      name: association.name,
    });
  }

  protected createFromForm(): IAssociation {
    return {
      ...new Association(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
