import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocalAssociationProvince } from '../local-association-province.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { LocalAssociationProvinceService } from '../service/local-association-province.service';
import { LocalAssociationProvinceDeleteDialogComponent } from '../delete/local-association-province-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-local-association-province',
  templateUrl: './local-association-province.component.html',
})
export class LocalAssociationProvinceComponent implements OnInit {
  localAssociationProvinces: ILocalAssociationProvince[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected localAssociationProvinceService: LocalAssociationProvinceService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.localAssociationProvinces = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.localAssociationProvinceService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<ILocalAssociationProvince[]>) => {
          this.isLoading = false;
          this.paginateLocalAssociationProvinces(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.localAssociationProvinces = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ILocalAssociationProvince): number {
    return item.id!;
  }

  delete(localAssociationProvince: ILocalAssociationProvince): void {
    const modalRef = this.modalService.open(LocalAssociationProvinceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.localAssociationProvince = localAssociationProvince;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateLocalAssociationProvinces(data: ILocalAssociationProvince[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.localAssociationProvinces.push(d);
      }
    }
  }
}
