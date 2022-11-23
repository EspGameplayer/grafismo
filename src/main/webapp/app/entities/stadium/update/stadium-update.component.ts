import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IStadium, Stadium } from '../stadium.model';
import { StadiumService } from '../service/stadium.service';

@Component({
  selector: 'jhi-stadium-update',
  templateUrl: './stadium-update.component.html',
})
export class StadiumUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    graphicsName: [null, [Validators.required]],
    location: [],
    capacity: [null, [Validators.min(0)]],
    fieldLength: [null, [Validators.min(0)]],
    fieldWidth: [null, [Validators.min(0)]],
  });

  constructor(protected stadiumService: StadiumService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stadium }) => {
      this.updateForm(stadium);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stadium = this.createFromForm();
    if (stadium.id !== undefined) {
      this.subscribeToSaveResponse(this.stadiumService.update(stadium));
    } else {
      this.subscribeToSaveResponse(this.stadiumService.create(stadium));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStadium>>): void {
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

  protected updateForm(stadium: IStadium): void {
    this.editForm.patchValue({
      id: stadium.id,
      name: stadium.name,
      graphicsName: stadium.graphicsName,
      location: stadium.location,
      capacity: stadium.capacity,
      fieldLength: stadium.fieldLength,
      fieldWidth: stadium.fieldWidth,
    });
  }

  protected createFromForm(): IStadium {
    return {
      ...new Stadium(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      graphicsName: this.editForm.get(['graphicsName'])!.value,
      location: this.editForm.get(['location'])!.value,
      capacity: this.editForm.get(['capacity'])!.value,
      fieldLength: this.editForm.get(['fieldLength'])!.value,
      fieldWidth: this.editForm.get(['fieldWidth'])!.value,
    };
  }
}
